/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator.calculators;

import com.topcoder.management.review.scorecalculator.AbstractScoreCalculator;
import com.topcoder.management.review.scorecalculator.AbstractScoreCalculatorTest;
import com.topcoder.management.review.scorecalculator.CalculationException;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.TestUtil;
import com.topcoder.util.config.ConfigManager;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Contains the unit tests for the BinaryScoreCalculator class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class BinaryScoreCalculatorTest extends AbstractScoreCalculatorTest {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The name of the configuration file containing all the configuration for unit testing this class.
     */
    private static final String CONFIG_FILENAME = "UnitTests/BinaryScoreCalculatorTestConfig.xml";

    /**
     * The configuration namespace that does not exist in the configuration file.
     */
    private static final String MISSING_NAMESPACE = "missing.namespace";

    /**
     * The configuration namespace where the positive_answer property is missing.
     */
    private static final String MISSING_POSITIVE_ANSWER_NAMESPACE = "missing.positive.answer";

    /**
     * The configuration namespace where the positive_answer property is an empty string.
     */
    private static final String EMPTY_POSITIVE_ANSWER_NAMESPACE = "empty.positive.answer";

    /**
     * The configuration namespace where the positive_answer property is a blank string.
     */
    private static final String BLANK_POSITIVE_ANSWER_NAMESPACE = "blank.positive.answer";

    /**
     * The configuration namespace where the negative_answer property is missing.
     */
    private static final String MISSING_NEGATIVE_ANSWER_NAMESPACE = "missing.negative.answer";

    /**
     * The configuration namespace where the negative_answer property is an empty string.
     */
    private static final String EMPTY_NEGATIVE_ANSWER_NAMESPACE = "empty.negative.answer";

    /**
     * The configuration namespace where the negative_answer property is a blank string.
     */
    private static final String BLANK_NEGATIVE_ANSWER_NAMESPACE = "blank.negative.answer";

    /**
     * The configuration namespace where the positive_answer property is equal to the negative_answer property.
     */
    private static final String SAME_POSITIVE_NEGATIVE_ANSWER_NAMESPACE = "same.positive.negative.answer";

    /**
     * The configuration namespace that contains a valid configuration.
     */
    private static final String VALID_NAMESPACE = "valid";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(BinaryScoreCalculatorTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // AbstractScoreCalculatorTest Methods

    /**
     * Creates a new BinaryScoreCalculator.
     *
     * @return  A new BinaryScoreCalculator.
     */
    protected AbstractScoreCalculator createInstance() {
        return new BinaryScoreCalculator();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SetUp/TearDown

    /**
     * Recreates the instances used during unit testing, and reloads the configuration file.
     *
     * @throws  Exception
     *          An unknown error occured during setup.
     */
    protected void setUp() throws Exception {
        super.setUp();
        clearNamespaces();
        ConfigManager.getInstance().add(CONFIG_FILENAME);
        getQuestion().setWeight(50.0f);
    }

    /**
     * Clears the configuration manager of our namespaces.
     *
     * @throws  Exception
     *          An unknown error occured during teardown.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        clearNamespaces();
    }

    /**
     * Clears the configuration manager of our namespaces.
     */
    private static void clearNamespaces() {
        TestUtil.clearNamespace(MISSING_POSITIVE_ANSWER_NAMESPACE);
        TestUtil.clearNamespace(EMPTY_POSITIVE_ANSWER_NAMESPACE);
        TestUtil.clearNamespace(BLANK_POSITIVE_ANSWER_NAMESPACE);
        TestUtil.clearNamespace(MISSING_NEGATIVE_ANSWER_NAMESPACE);
        TestUtil.clearNamespace(EMPTY_NEGATIVE_ANSWER_NAMESPACE);
        TestUtil.clearNamespace(BLANK_NEGATIVE_ANSWER_NAMESPACE);
        TestUtil.clearNamespace(SAME_POSITIVE_NEGATIVE_ANSWER_NAMESPACE);
        TestUtil.clearNamespace(VALID_NAMESPACE);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor() Tests

    /**
     * Ensures that the zero argument constructor properly sets the positiveAnswer field, by checking that the
     * evaluateItem returns the correct score for a positive answer.
     *
     * @throws  ScoreCalculatorException
     *          An unknown error occured.
     */
    public void test0CtorWorksOnPositiveAnswer() throws ScoreCalculatorException {
        getItem().setAnswer("Yes");

        assertEquals(
                "The positive answer 'Yes' should result in a score of 1.",
                1.0f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the zero argument constructor properly sets the negativeAnswer field, by checking that the
     * evaluateItem returns the correct score for a negative answer.
     *
     * @throws  ScoreCalculatorException
     *          An unknown error occured.
     */
    public void test0CtorWorksOnNegativeAnswer() throws ScoreCalculatorException {
        getItem().setAnswer("No");

        assertEquals(
                "The negative answer 'No' should result in a score of 0.",
                0.0f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the zero argument constructor properly sets the positiveAnswer and negativeAnswer fields, by
     * checking that the evaluateItem throws a ScoreCalculatorException when given neither the positive nor
     * negative answer.
     */
    public void test0CtorThrowsOnUnknownAnswer() {
        getItem().setAnswer("Maybe");

        checkEvaulateItemThrowsSCE("an answer that is neither the positive nor negative answer.");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String) Tests

    /**
     * Ensures that the one argument constructor throws an IllegalArgumentException when given a null namespace.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void test1CtorThrowsOnNullNamespace() throws ConfigurationException {
        check1CtorThrowsIAE(null, "a null namespace.");
    }

    /**
     * Ensures that the one argument constructor throws an IllegalArgumentException when given an empty namespace.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void test1CtorThrowsOnEmptyNamespace() throws ConfigurationException {
        check1CtorThrowsIAE("", "an empty namespace.");
    }

    /**
     * Ensures that the one argument constructor throws an IllegalArgumentException when given a blank namespace.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void test1CtorThrowsOnBlankNamespace() throws ConfigurationException {
        check1CtorThrowsIAE(" ", "a blank namespace.");
    }

    /**
     * Helper method to check that the one argument constructor throws an IllegalArgumentException when given
     * the specified namespace.
     *
     * @param   namespace
     *          The namespace that should cause an IllegalArgumentException.
     * @param   message
     *          The message to use in the error message if the unit test fails.
     *
     * @throws  ConfigurationException
     *          An unknown configuration error occurred.
     */
    private void check1CtorThrowsIAE(String namespace, String message) throws ConfigurationException {
        try {
            new BinaryScoreCalculator(namespace);
            fail("An IllegalArgumentException is expected when " + message);
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when the namespace is missing
     * from the Configuration Manager.
     */
    public void test1CtorThrowsOnMissingNamespace() {
        check1CtorThrowsCE(MISSING_NAMESPACE, "the namespace does not exist.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when the namespace is missing
     * a positive_answer property.
     */
    public void test1CtorThrowsOnMissingPositiveAnswer() {
        check1CtorThrowsCE(MISSING_POSITIVE_ANSWER_NAMESPACE, "the positive_answer property is missing.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when the namespace contains a
     * positive_answer property that is an empty string.
     */
    public void test1CtorThrowsOnEmptyPositiveAnswer() {
        check1CtorThrowsCE(EMPTY_POSITIVE_ANSWER_NAMESPACE, "the positive_answer property is an empty string.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when the namespace contains a
     * positive_answer property that is a blank string.
     */
    public void test1CtorThrowsOnBlankPositiveAnswer() {
        check1CtorThrowsCE(BLANK_POSITIVE_ANSWER_NAMESPACE, "the positive_answer property is a blank string.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when the namespace is missing
     * a negative_answer property.
     */
    public void test1CtorThrowsOnMissingNegativeAnswer() {
        check1CtorThrowsCE(MISSING_NEGATIVE_ANSWER_NAMESPACE, "the negative_answer property is missing.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when the namespace contains a
     * negative_answer property that is an empty string.
     */
    public void test1CtorThrowsOnEmptyNegativeAnswer() {
        check1CtorThrowsCE(EMPTY_NEGATIVE_ANSWER_NAMESPACE, "the negative_answer property is an empty string.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when the namespace contains a
     * negative_answer property that is a blank string.
     */
    public void test1CtorThrowsOnBlankNegativeAnswer() {
        check1CtorThrowsCE(BLANK_NEGATIVE_ANSWER_NAMESPACE, "the negative_answer property is a blank string.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when the namespace contains the
     * exact same positive_answer and negative_answer property.
     */
    public void test1CtorThrowsOnSamePositiveNegativeAnswers() {
        check1CtorThrowsCE(
                SAME_POSITIVE_NEGATIVE_ANSWER_NAMESPACE,
                "the positve_answer and negative_answer property are the same.");
    }

    /**
     * Helper method to check that the one argument constructor throws a ConfigurationException when given the
     * specified namespace.
     *
     * @param   namespace
     *          The namespace that should cause a ConfigurationException.
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void check1CtorThrowsCE(String namespace, String message) {
        try {
            new BinaryScoreCalculator(namespace);
            fail("A ConfigurationException is expected when " + message);
        } catch (ConfigurationException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the one argument constructor properly sets the positiveAnswer field, by checking that the
     * evaluateItem returns the correct score for a positive answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test1CtorWorksOnPositiveAnswer() throws CalculationException {
        setInstance(new BinaryScoreCalculator(VALID_NAMESPACE));
        getItem().setAnswer("Pass");

        assertEquals(
                "The positive answer 'Pass' should result in a score of 1.",
                1.0f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the one argument constructor properly sets the negativeAnswer field, by checking that the
     * evaluateItem returns the correct score for a negative answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test1CtorWorksOnNegativeAnswer() throws CalculationException {
        setInstance(new BinaryScoreCalculator(VALID_NAMESPACE));
        getItem().setAnswer("Fail");

        assertEquals(
                "The negative answer 'Fail' should result in a score of 0.",
                0.0f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the one argument constructor properly sets the positiveAnswer and negativeAnswer fields, by
     * checking that the evaluateItem throws a ScoreCalculatorException when given neither the positive nor
     * negative answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test1CtorThrowsOnUnknownAnswer() throws CalculationException {
        setInstance(new BinaryScoreCalculator(VALID_NAMESPACE));
        getItem().setAnswer("Maybe");

        checkEvaulateItemThrowsSCE("an answer that is neither the positive nor negative answer.");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String, String) Tests

    /**
     * Ensures that the two argument constructor throws an IllegalArgumentException when given a null positive
     * answer.
     */
    public void test2CtorThrowsOnNullPositiveAnswer() {
        check2CtorThrowsIAE(null, "False", "a null positive answer.");
    }

    /**
     * Ensures that the two argument constructor throws an IllegalArgumentException when given an empty positive
     * answer.
     */
    public void test2CtorThrowsOnEmptyPositiveAnswer() {
        check2CtorThrowsIAE("", "False", "an empty positive answer.");
    }

    /**
     * Ensures that the two argument constructor throws an IllegalArgumentException when given a blank positive
     * answer.
     */
    public void test2CtorThrowsOnBlankPositiveAnswer() {
        check2CtorThrowsIAE(" ", "False", "a blank positive answer.");
    }

    /**
     * Ensures that the two argument constructor throws an IllegalArgumentException when given a null negative
     * answer.
     */
    public void test2CtorThrowsOnNullNegativeAnswer() {
        check2CtorThrowsIAE("True", null, "a null negative answer.");
    }

    /**
     * Ensures that the two argument constructor throws an IllegalArgumentException when given an empty negative
     * answer.
     */
    public void test2CtorThrowsOnEmptyNegativeAnswer() {
        check2CtorThrowsIAE("True", "", "an empty negative answer.");
    }

    /**
     * Ensures that the two argument constructor throws an IllegalArgumentException when given a blank negative
     * answer.
     */
    public void test2CtorThrowsOnBlankNegativeAnswer() {
        check2CtorThrowsIAE("True", " ", "a blank negative answer.");
    }

    /**
     * Ensures that the two argument constructor throws an IllegalArgumentException when given the same positive
     * and negative answer.
     */
    public void test2CtorThrowsOnSamePositiveNegativeAnswer() {
        check2CtorThrowsIAE("True", "True", "the same positive and negative answer.");
    }

    /**
     * Helper method to check that the two argument constructor throws an IllegalArgumentException when given
     * the specified arguments.
     *
     * @param   positiveAnswer
     *          The positive answer that should cause an IllegalArgumentException.
     * @param   negativeAnswer
     *          The negative answer that should cause an IllegalArgumentException.
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void check2CtorThrowsIAE(String positiveAnswer, String negativeAnswer, String message) {
        try {
            new BinaryScoreCalculator(positiveAnswer, negativeAnswer);
            fail("An IllegalArgumentException is expected when " + message);
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the two argument constructor properly sets the positiveAnswer field, by checking that the
     * evaluateItem returns the correct score for a positive answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test2CtorWorksOnPositiveAnswer() throws CalculationException {
        setInstance(new BinaryScoreCalculator("True", "False"));
        getItem().setAnswer("True");

        assertEquals(
                "The positive answer 'True' should result in a score of 1.",
                1.0f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the two argument constructor properly sets the negativeAnswer field, by checking that the
     * evaluateItem returns the correct score for a negative answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test2CtorWorksOnNegativeAnswer() throws CalculationException {
        setInstance(new BinaryScoreCalculator("True", "False"));
        getItem().setAnswer("False");

        assertEquals(
                "The negative answer 'False' should result in a score of 0.",
                0.0f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the two argument constructor properly sets the positiveAnswer and negativeAnswer fields, by
     * checking that the evaluateItem throws a ScoreCalculatorException when given neither the positive nor
     * negative answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test2CtorThrowsOnUnknownAnswer() throws CalculationException {
        setInstance(new BinaryScoreCalculator("True", "False"));
        getItem().setAnswer("TrueFalse");

        checkEvaulateItemThrowsSCE("an answer that is neither the positive nor negative answer.");
    }
}
