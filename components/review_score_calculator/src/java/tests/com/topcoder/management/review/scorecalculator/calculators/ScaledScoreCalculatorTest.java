/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator.calculators;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.management.review.scorecalculator.AbstractScoreCalculator;
import com.topcoder.management.review.scorecalculator.AbstractScoreCalculatorTest;
import com.topcoder.management.review.scorecalculator.CalculationException;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.TestUtil;
import com.topcoder.util.config.ConfigManager;

/**
 * Contains the unit tests for the ScaledScoreCalculator class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ScaledScoreCalculatorTest extends AbstractScoreCalculatorTest {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The name of the configuration file containing all the configuration for unit testing this class.
     */
    private static final String CONFIG_FILENAME = "UnitTests/ScaledScoreCalculatorTestConfig.xml";

    /**
     * The configuration namespace that does not exist in the configuration file.
     */
    private static final String MISSING_NAMESPACE = "missing.namespace";

    /**
     * The configuration namespace where the default_scale property is an empty string.
     */
    private static final String EMPTY_DEFAULT_SCALE_NAMESPACE = "empty.default.scale";

    /**
     * The configuration namespace where the default_scale property is a blank string.
     */
    private static final String BLANK_DEFAULT_SCALE_NAMESPACE = "blank.default.scale";

    /**
     * The configuration namespace where the default_scale property is an invalid Long.
     */
    private static final String INVALID_DEFAULT_SCALE_NAMESPACE = "invalid.default.scale";

    /**
     * The configuration namespace where the default_scale property is negative.
     */
    private static final String NEGATIVE_DEFAULT_SCALE_NAMESPACE = "negative.default.scale";

    /**
     * The configuration namespace where the default_scale property is a zero.
     */
    private static final String ZERO_DEFAULT_SCALE_NAMESPACE = "zero.default.scale";

    /**
     * The configuration namespace where the default_scale property is too large of a Long.
     */
    private static final String TOO_LARGE_DEFAULT_SCALE_NAMESPACE = "too.large.default.scale";

    /**
     * The configuration namespace that contains a valid configuration.
     */
    private static final String VALID_NAMESPACE = "valid";

    /**
     * The configuration namespace that contains a valid configuration (with a missing default_scale property).
     */
    private static final String VALID_MISSING_NAMESPACE = "valid.missing";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(ScaledScoreCalculatorTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // AbstractScoreCalculatorTest Methods

    /**
     * Creates a new ScaledScoreCalculator.
     *
     * @return  A new ScaledScoreCalculator.
     */
    protected AbstractScoreCalculator createInstance() {
        return new ScaledScoreCalculator();
    }

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
        TestUtil.clearNamespace(EMPTY_DEFAULT_SCALE_NAMESPACE);
        TestUtil.clearNamespace(BLANK_DEFAULT_SCALE_NAMESPACE);
        TestUtil.clearNamespace(INVALID_DEFAULT_SCALE_NAMESPACE);
        TestUtil.clearNamespace(NEGATIVE_DEFAULT_SCALE_NAMESPACE);
        TestUtil.clearNamespace(ZERO_DEFAULT_SCALE_NAMESPACE);
        TestUtil.clearNamespace(TOO_LARGE_DEFAULT_SCALE_NAMESPACE);
        TestUtil.clearNamespace(VALID_NAMESPACE);
        TestUtil.clearNamespace(VALID_MISSING_NAMESPACE);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor() Tests

    /**
     * Ensures that the zero argument constructor properly sets the defaultScale field, by checking that the
     * evaluateItem returns the correct score for a 'value/scale' answer.
     *
     * @throws  ScoreCalculatorException
     *          An unknown error occured.
     */
    public void test0CtorWorksOnValueScaleAnswer() throws ScoreCalculatorException {
        getItem().setAnswer("1/2");
        getQuestion().setWeight(50.0f);

        assertEquals(
                "The answer '1/2' with should result in a score of 0.5.",
                0.5f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the zero argument constructor properly sets the defaultScale field, by checking that the
     * evaluateItem throws a ScoreCalculatorException for a 'value' answer.
     *
     * @throws  ScoreCalculatorException
     *          An unknown error occured.
     */
    public void test0CtorThrowsOnValueAnswer() throws ScoreCalculatorException {
        getItem().setAnswer("1");
        getQuestion().setWeight(50.0f);

        checkEvaulateItemThrowsSCE("a 'value' answer when the defaultScale is uninitialized.");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(long) Tests

    /**
     * Ensures that the one argument long constructor throws an IllegalArgumentException when given a negative scale.
     */
    public void test1LongCtorThrowsOnNegativeScale() {
        try {
            new ScaledScoreCalculator(-1);
            fail("An IllegalArgumentException is expected when given a negative scale.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * test that when scale is zero the only allowed value is zero
     * @throws ScoreCalculatorException 
     */
    public void test1ZeroScale() throws ScoreCalculatorException {
    	ScaledScoreCalculator scaledScoreCalculator = new ScaledScoreCalculator(0);
    	assertEquals((float) 0, scaledScoreCalculator.evaluateAnswer("0"));
        try {
            scaledScoreCalculator.evaluateAnswer("1");
            fail("An IllegalArgumentException is expected when given scale = 0 and value != 0");
        } catch (ScoreCalculatorException ex) {
            // Good!
        }
        try {
            scaledScoreCalculator.evaluateAnswer("-1");
            fail("An IllegalArgumentException is expected when given scale = 0 and value != 0");
        } catch (ScoreCalculatorException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the one argument long constructor properly sets the defaultScale field, by checking that the
     * evaluateItem returns the correct score for a 'value' answer.
     *
     * @throws  ScoreCalculatorException
     *          An unknown error occured.
     */
    public void test1LongCtorWorksOnValueAnswer() throws ScoreCalculatorException {
        setInstance(new ScaledScoreCalculator(4));
        getItem().setAnswer("3");

        assertEquals(
                "The answer '3' with a default scale of 4 should result in a score of 0.75.",
                0.75f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the one argument long constructor properly sets the defaultScale field, by checking that the
     * evaluateItem returns the correct score for a 'value/scale' answer.
     *
     * @throws  ScoreCalculatorException
     *          An unknown error occured.
     */
    public void test1LongCtorWorksOnValueScaleAnswer() throws ScoreCalculatorException {
        setInstance(new ScaledScoreCalculator(4));
        getItem().setAnswer("3/6");

        assertEquals(
                "The answer '3/6' with a default scale of 4 should result in a score of 0.5.",
                0.5f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String) Tests

    /**
     * Ensures that the one argument String constructor throws an IllegalArgumentException when given a null
     * namespace.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void test1StringCtorThrowsOnNullNamespace() throws ConfigurationException {
        check1StringCtorThrowsIAE(null, "a null namespace.");
    }

    /**
     * Ensures that the one argument String constructor throws an IllegalArgumentException when given an empty
     * namespace.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void test1StringCtorThrowsOnEmptyNamespace() throws ConfigurationException {
        check1StringCtorThrowsIAE("", "an empty namespace.");
    }

    /**
     * Ensures that the one argument String constructor throws an IllegalArgumentException when given a blank
     * namespace.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void test1StringCtorThrowsOnBlankNamespace() throws ConfigurationException {
        check1StringCtorThrowsIAE(" ", "a blank namespace.");
    }

    /**
     * Helper method to check that the one argument String constructor throws an IllegalArgumentException when given
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
    private void check1StringCtorThrowsIAE(String namespace, String message) throws ConfigurationException {
        try {
            new ScaledScoreCalculator(namespace);
            fail("An IllegalArgumentException is expected when " + message);
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the one argument String constructor throws a ConfigurationException when the namespace is missing
     * from the Configuration Manager.
     */
    public void test1StringCtorThrowsOnMissingNamespace() {
        check1StringCtorThrowsCE(MISSING_NAMESPACE, "the namespace does not exist.");
    }

    /**
     * Ensures that the one argument String constructor throws a ConfigurationException when the namespace contains
     * a default_scale property that is an empty string.
     */
    public void test1CtorThrowsOnEmptyDefaultScale() {
        check1StringCtorThrowsCE(EMPTY_DEFAULT_SCALE_NAMESPACE, "the default_scale property is an empty string.");
    }

    /**
     * Ensures that the one argument String constructor throws a ConfigurationException when the namespace contains
     * a default_scale property that is a blank string.
     */
    public void test1CtorThrowsOnBlankDefaultScale() {
        check1StringCtorThrowsCE(BLANK_DEFAULT_SCALE_NAMESPACE, "the default_scale property is a blank string.");
    }

    /**
     * Ensures that the one argument String constructor throws a ConfigurationException when the namespace contains
     * a default_scale property that is a blank string.
     */
    public void test1CtorThrowsOnInvalidDefaultScale() {
        check1StringCtorThrowsCE(INVALID_DEFAULT_SCALE_NAMESPACE, "the default_scale property is an invalid Long.");
    }

    /**
     * Ensures that the one argument String constructor throws a ConfigurationException when the namespace contains
     * a negative default_scale property.
     */
    public void test1CtorThrowsOnNegativeDefaultScale() {
        check1StringCtorThrowsCE(NEGATIVE_DEFAULT_SCALE_NAMESPACE, "the default_scale property is negative.");
    }

    /**
     * Ensures that the one argument String constructor throws a ConfigurationException when the namespace
     * contains a zero default_scale property.
     */
    public void test1CtorThrowsOnZeroDefaultScale() {
        check1StringCtorThrowsCE(ZERO_DEFAULT_SCALE_NAMESPACE, "the default_scale property is zero.");
    }

    /**
     * Ensures that the one argument String constructor throws a ConfigurationException when the namespace
     * contains too large a default_scale property.
     */
    public void test1CtorThrowsOnTooLargeDefaultScale() {
        check1StringCtorThrowsCE(TOO_LARGE_DEFAULT_SCALE_NAMESPACE, "the default_scale property is to large.");
    }

    /**
     * Helper method to check that the one argument String constructor throws a ConfigurationException when given
     * the specified namespace.
     *
     * @param   namespace
     *          The namespace that should cause a ConfigurationException.
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void check1StringCtorThrowsCE(String namespace, String message) {
        try {
            new ScaledScoreCalculator(namespace);
            fail("A ConfigurationException is expected when " + message);
        } catch (ConfigurationException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the one argument String constructor properly sets the defaultScale field, by checking that the
     * evaluateItem returns the correct score for a 'value' answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test1StringCtorValidWorksOnValueAnswer() throws CalculationException {
        setInstance(new ScaledScoreCalculator(VALID_NAMESPACE));
        getItem().setAnswer("3");

        assertEquals(
                "The answer '3' with a default scale of 4 should result in a score of 0.75.",
                0.75f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the one argument String constructor properly sets the defaultScale field, by checking that the
     * evaluateItem returns the correct score for a 'value/scale' answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test1StringCtorValidWorksOnValueScaleAnswer() throws CalculationException {
        setInstance(new ScaledScoreCalculator(VALID_NAMESPACE));
        getItem().setAnswer("3/6");

        assertEquals(
                "The answer '3/6' with a default scale of 4 should result in a score of 0.5.",
                0.5f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /**
     * Ensures that the one argument String constructor properly sets the defaultScale field (with a missing
     * default_scale property), by checking that the evaluateItem throws a ScoreCalculatorException.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test1StringCtorValidMissingThrowsOnValueAnswer() throws CalculationException {
        setInstance(new ScaledScoreCalculator(VALID_MISSING_NAMESPACE));
        getItem().setAnswer("3");

        checkEvaulateItemThrowsSCE("a 'value' answer when the defaultScale is uninitialized.");
    }

    /**
     * Ensures that the one argument String constructor properly sets the defaultScale field (with a missing
     * default_scale property), by checking that the evaluateItem returns the correct score for a 'value/scale'
     * answer.
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void test1StringCtorValidMissingWorksOnValueScaleAnswer() throws CalculationException {
        setInstance(new ScaledScoreCalculator(VALID_MISSING_NAMESPACE));
        getItem().setAnswer("3/6");

        assertEquals(
                "The answer '3/6' with a default scale of 4 should result in a score of 0.5.",
                0.5f, getInstance().evaluateItem(getItem(), getQuestion()), 1e-9f);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // evaluateItem(Item, Question) Tests

    /**
     * Ensures that the evaluateItem method throws a ScoreCalculatorException when given an improperly formatted
     * answer.
     */
    public void testEvaluateItemThrowsOnInvalidAnswer() {
        getItem().setAnswer("TopCoder/TopCoder");
        checkEvaulateItemThrowsSCE("an answer that is not of the proper format 'value' or 'value/scale'.");
    }

    /**
     * Ensures that the evaluateItem method throws a ScoreCalculatorException when given a negative value.
     */
    public void testEvaluateItemThrowsOnNegativeValue() {
        getItem().setAnswer("-1/1");
        checkEvaulateItemThrowsSCE("an answer that has a negative value.");
    }

    /**
     * Ensures that the evaluateItem method throws a ScoreCalculatorException when given a value that is larger
     * than the scale.
     */
    public void testEvaluateItemThrowsOnValueTooLarge() {
        getItem().setAnswer("2/1");
        checkEvaulateItemThrowsSCE("an answer that has a value that is larger than the scale.");
    }

    /**
     * Ensures that the evaluateItem method throws a ScoreCalculatorException when given a negative scale.
     */
    public void testEvaluateItemThrowsOnNegativeScale() {
        getItem().setAnswer("1/-1");
        checkEvaulateItemThrowsSCE("an answer that has a negative scale.");
    }

    /**
     * Ensures that the evaluateItem method throws a ScoreCalculatorException when given a zero scale.
     */
    public void testEvaluateItemThrowsOnZeroScale() {
        getItem().setAnswer("1/0");
        checkEvaulateItemThrowsSCE("an answer that has a zero scale.");
    }

    /**
     * Ensures that the evaluateItem method throws a ScoreCalculatorException when given a value that is too
     * big for a Long.
     */
    public void testEvaluateItemThrowsOnSuperLargeValue() {
        setInstance(new ScaledScoreCalculator(1));
        getItem().setAnswer("9223372036854775808");
        checkEvaulateItemThrowsSCE("an answer that has a Long value that overflows.");
    }

    /**
     * Ensures that the evaluateItem method throws a ScoreCalculatorException when given a scale that is too
     * big for a Long.
     */
    public void testEvaluateItemThrowsOnSuperLargeScale() {
        getItem().setAnswer("9223372036854775807/9223372036854775808");
        checkEvaulateItemThrowsSCE("an answer that has a Long scale that overflows.");
    }
}
