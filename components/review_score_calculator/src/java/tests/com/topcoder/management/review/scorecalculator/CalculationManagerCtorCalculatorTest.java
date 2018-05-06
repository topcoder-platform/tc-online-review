/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator;
import com.topcoder.util.cache.Cache;
import com.topcoder.util.cache.SimpleCache;
import com.topcoder.util.config.ConfigManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains the constructor and calculators mutator unit tests for the CalculationManager class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class CalculationManagerCtorCalculatorTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The name of the configuration file containing all the configuration for unit testing this class.
     */
    private static final String CONFIG_FILENAME = "UnitTests/CalculationManagerTestConfig.xml";

    /**
     * The default configuration namespace.
     */
    private static final String DEFAULT_NAMESPACE =
        "com.topcoder.management.review.scorecalculator.CalculationManager";

    /**
     * The configuration namespace that does not exist in the configuration file.
     */
    private static final String MISSING_NAMESPACE = "missing.namespace";

    /**
     * The configuration namespace where the builder_class property is missing.
     */
    private static final String MISSING_BUILDER_CLASS_NAMESPACE = "missing.builder.class";

    /**
     * The configuration namespace where the builder_class property is empty.
     */
    private static final String EMPTY_BUILDER_CLASS_NAMESPACE = "empty.builder.class";

    /**
     * The configuration namespace where the builder_class property is blank.
     */
    private static final String BLANK_BUILDER_CLASS_NAMESPACE = "blank.builder.class";

    /**
     * The configuration namespace where the builder_class property doesn't refer to an existing class.
     */
    private static final String INVALID_BUILDER_CLASS_NAMESPACE = "invalid.builder.class";

    /**
     * The configuration namespace where the builder_class property doesn't derive from ScorecardMatrixBuilder.
     */
    private static final String UNDERIVED_BUILDER_CLASS_NAMESPACE = "underived.builder.class";

    /**
     * The configuration namespace where the builder_class property refers to a class with no visible zero
     * argument constructor.
     */
    private static final String NOEXIST_BUILDER_CLASS_CTOR_NAMESPACE = "noexist.builder.class.ctor";

    /**
     * The configuration namespace where the builder_class property refers to a class with no visible namespace
     * constructor.
     */
    private static final String NOEXIST_BUILDER_CLASS_NS_CTOR_NAMESPACE = "noexist.builder.class.ns.ctor";

    /**
     * The configuration namespace where the calculators property is missing.
     */
    private static final String MISSING_CALCULATORS_NAMESPACE = "missing.calculators";

    /**
     * The configuration namespace where the question_type property is missing.
     */
    private static final String MISSING_CALC_QUESTION_TYPE_NAMESPACE = "missing.calc.question.type";

    /**
     * The configuration namespace where the question_type property is not a valid Long.
     */
    private static final String INVALID_CALC_QUESTION_TYPE_NAMESPACE = "invalid.calc.question.type";

    /**
     * The configuration namespace where the question_type property is too large for a Long.
     */
    private static final String TOO_LARGE_CALC_QUESTION_TYPE_NAMESPACE = "too.large.calc.question.type";

    /**
     * The configuration namespace where the question_type property is negative.
     */
    private static final String NEGATIVE_CALC_QUESTION_TYPE_NAMESPACE = "negative.calc.question.type";

    /**
     * The configuration namespace where the question_type property is zero.
     */
    private static final String ZERO_CALC_QUESTION_TYPE_NAMESPACE = "zero.calc.question.type";

    /**
     * The configuration namespace where the question_type property is a duplicate.
     */
    private static final String DUPLICATE_CALC_QUESTION_TYPE_NAMESPACE = "duplicate.calc.question.type";

    /**
     * The configuration namespace where the calculator class property is missing.
     */
    private static final String MISSING_CALCULATOR_CLASS_NAMESPACE = "missing.calculator.class";

    /**
     * The configuration namespace where the calculator class property is empty.
     */
    private static final String EMPTY_CALCULATOR_CLASS_NAMESPACE = "empty.calculator.class";

    /**
     * The configuration namespace where the calculator class property is blank.
     */
    private static final String BLANK_CALCULATOR_CLASS_NAMESPACE = "blank.calculator.class";

    /**
     * The configuration namespace where the calculator class property doesn't refer to an existing class.
     */
    private static final String INVALID_CALCULATOR_CLASS_NAMESPACE = "invalid.calculator.class";

    /**
     * The configuration namespace where the calculator class property doesn't derive from ScoreCalculator.
     */
    private static final String UNDERIVED_CALCULATOR_CLASS_NAMESPACE = "underived.calculator.class";

    /**
     * The configuration namespace where the calculator class property refers to a class with no visible zero
     * argument constructor.
     */
    private static final String NOEXIST_CALCULATOR_CLASS_CTOR_NAMESPACE = "noexist.calculator.class.ctor";

    /**
     * The configuration namespace where the calculator class property refers to a class with no visible
     * namespace constructor.
     */
    private static final String NOEXIST_CALCULATOR_CLASS_NS_CTOR_NAMESPACE = "noexist.calculator.class.ns.ctor";

    /**
     * The configuration namespace that contains a valid configuration.
     */
    private static final String VALID_NAMESPACE = "valid";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * The instance to be used for unit testing.
     */
    private CalculationManager instance = null;

    /**
     * The list of question types to use in unit testing.
     */
    private long[] questionTypes = null;

    /**
     * The list of score calculators to use in unit testing.
     */
    private ScoreCalculator[] calculators = null;

    /**
     * The list of builders to use in unit testing.
     */
    private ScorecardMatrixBuilder builder = null;

    /**
     * The use caching switch to use in unit testing.
     */
    private boolean useCaching = false;

    /**
     * The cache to use in unit testing.
     */
    private Cache cache = null;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(CalculationManagerCtorCalculatorTest.class);
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

        questionTypes = new long[] {1};
        calculators = new ScoreCalculator[] {new BinaryScoreCalculator()};
        builder = new DefaultScorecardMatrixBuilder();
        useCaching = false;
        cache = new SimpleCache();

        instance = new CalculationManager(new long[0], new ScoreCalculator[0], builder, false);
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
        TestUtil.clearNamespace(DEFAULT_NAMESPACE);
        TestUtil.clearNamespace(MISSING_BUILDER_CLASS_NAMESPACE);
        TestUtil.clearNamespace(EMPTY_BUILDER_CLASS_NAMESPACE);
        TestUtil.clearNamespace(BLANK_BUILDER_CLASS_NAMESPACE);
        TestUtil.clearNamespace(INVALID_BUILDER_CLASS_NAMESPACE);
        TestUtil.clearNamespace(UNDERIVED_BUILDER_CLASS_NAMESPACE);
        TestUtil.clearNamespace(NOEXIST_BUILDER_CLASS_CTOR_NAMESPACE);
        TestUtil.clearNamespace(NOEXIST_BUILDER_CLASS_NS_CTOR_NAMESPACE);
        TestUtil.clearNamespace(MISSING_CALCULATORS_NAMESPACE);
        TestUtil.clearNamespace(MISSING_CALC_QUESTION_TYPE_NAMESPACE);
        TestUtil.clearNamespace(INVALID_CALC_QUESTION_TYPE_NAMESPACE);
        TestUtil.clearNamespace(TOO_LARGE_CALC_QUESTION_TYPE_NAMESPACE);
        TestUtil.clearNamespace(NEGATIVE_CALC_QUESTION_TYPE_NAMESPACE);
        TestUtil.clearNamespace(ZERO_CALC_QUESTION_TYPE_NAMESPACE);
        TestUtil.clearNamespace(DUPLICATE_CALC_QUESTION_TYPE_NAMESPACE);
        TestUtil.clearNamespace(MISSING_CALCULATOR_CLASS_NAMESPACE);
        TestUtil.clearNamespace(EMPTY_CALCULATOR_CLASS_NAMESPACE);
        TestUtil.clearNamespace(BLANK_CALCULATOR_CLASS_NAMESPACE);
        TestUtil.clearNamespace(INVALID_CALCULATOR_CLASS_NAMESPACE);
        TestUtil.clearNamespace(UNDERIVED_CALCULATOR_CLASS_NAMESPACE);
        TestUtil.clearNamespace(NOEXIST_CALCULATOR_CLASS_CTOR_NAMESPACE);
        TestUtil.clearNamespace(NOEXIST_CALCULATOR_CLASS_NS_CTOR_NAMESPACE);
        TestUtil.clearNamespace(VALID_NAMESPACE);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor() Tests

    // All failure conditions are checked in the Constructor(String) tests because it would be redundant to have
    // them for the default constructor.

    /**
     * Ensures that the zero argument constructor sets the calculators field properly, by checking that the
     * getScoreCalculator method returns an object of the correct type.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void test0CtorSetScoreCalculator() throws ConfigurationException {
        CalculationManager manager = new CalculationManager();

        assertTrue(
                "Question type 1 should be a BinaryScoreCalculator.",
                manager.getScoreCalculator(1) instanceof BinaryScoreCalculator);
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
     * Helper method to check that the one argument constructor throws an IllegalArgumentException when given the
     * specified namespace.
     *
     * @param   namespace
     *          The namespace that should cause an IllegalArgumentException.
     * @param   message
     *          The message in the error message if the unit test fails.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    private void check1CtorThrowsIAE(String namespace, String message) throws ConfigurationException {
        try {
            new CalculationManager(namespace);
            fail("An IllegalArgumentException is expected when given " + message);
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace that
     * doesn't exist.
     */
    public void test1CtorThrowsOnMissingNamespace() {
        check1CtorThrowsCE(MISSING_NAMESPACE, "a namespace that doesn't exist.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * missing builder_class property.
     */
    public void test1CtorThrowsOnMissingBuilderClass() {
        check1CtorThrowsCE(MISSING_BUILDER_CLASS_NAMESPACE, "a missing builder_class property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with an
     * empty builder_class property.
     */
    public void test1CtorThrowsOnEmptyBuilderClass() {
        check1CtorThrowsCE(EMPTY_BUILDER_CLASS_NAMESPACE, "an empty builder_class property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * blank builder_class property.
     */
    public void test1CtorThrowsOnBlankBuilderClass() {
        check1CtorThrowsCE(BLANK_BUILDER_CLASS_NAMESPACE, "a blank builder_class property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * builder_class property that doesn't refer to a real class.
     */
    public void test1CtorThrowsOnInvalidBuilderClass() {
        check1CtorThrowsCE(INVALID_BUILDER_CLASS_NAMESPACE,
                "a builder_class property that doesn't refer to a real class.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * builder_class property that doesn't derive from ScorecardMatrixBuilder.
     */
    public void test1CtorThrowsOnUnderivedBuilderClass() {
        check1CtorThrowsCE(UNDERIVED_BUILDER_CLASS_NAMESPACE,
                "a builder_class property that doesn't derive from ScorecardMatrixBuilder.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * builder_class property that doesn't have a public zero arg constructor.
     */
    public void test1CtorThrowsOnNoExistBuilderClassCtor() {
        check1CtorThrowsCE(NOEXIST_BUILDER_CLASS_CTOR_NAMESPACE,
                "a builder_class property that doesn't have a public zero arg constructor.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * builder_class property that doesn't have a public namespace constructor.
     */
    public void test1CtorThrowsOnPrivateBuilderClassNSCtor() {
        check1CtorThrowsCE(NOEXIST_BUILDER_CLASS_NS_CTOR_NAMESPACE,
                "a builder_class property that doesn't have a public namespace constructor.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * missing calculators property.
     */
    public void test1CtorThrowsOnMissingCalculators() {
        check1CtorThrowsCE(MISSING_CALCULATORS_NAMESPACE, "a missing calculators property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * missing question_type property.
     */
    public void test1CtorThrowsOnMissingCalcQuestionType() {
        check1CtorThrowsCE(MISSING_CALC_QUESTION_TYPE_NAMESPACE, "a missing question_type property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * question_type property that is not a valid Long.
     */
    public void test1CtorThrowsOnInvalidCalcQuestionType() {
        check1CtorThrowsCE(INVALID_CALC_QUESTION_TYPE_NAMESPACE,
                "a question_type property that is not a valid Long.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * question_type property that is too large for a Long.
     */
    public void test1CtorThrowsOnTooLargeCalcQuestionType() {
        check1CtorThrowsCE(TOO_LARGE_CALC_QUESTION_TYPE_NAMESPACE,
                "a question_type property that is too large for a Long.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * question_type property that is negative.
     */
    public void test1CtorThrowsOnNegativeCalcQuestionType() {
        check1CtorThrowsCE(NEGATIVE_CALC_QUESTION_TYPE_NAMESPACE, "a question_type property that is negative.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * question_type property that is zero.
     */
    public void test1CtorThrowsOnZeroCalcQuestionType() {
        check1CtorThrowsCE(ZERO_CALC_QUESTION_TYPE_NAMESPACE, "a question_type property that is zero.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * duplicate question_type property.
     */
    public void test1CtorThrowsOnDuplicateCalcQuestionType() {
        check1CtorThrowsCE(DUPLICATE_CALC_QUESTION_TYPE_NAMESPACE, "a duplicate question_type property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * missing calculator class property.
     */
    public void test1CtorThrowsOnMissingCalculatorClass() {
        check1CtorThrowsCE(MISSING_CALCULATOR_CLASS_NAMESPACE, "a missing calculator class property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with an
     * empty calculator class property.
     */
    public void test1CtorThrowsOnEmptyCalculatorClass() {
        check1CtorThrowsCE(EMPTY_CALCULATOR_CLASS_NAMESPACE, "an empty calculator class property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * blank calculator class property.
     */
    public void test1CtorThrowsOnBlankCalculatorClass() {
        check1CtorThrowsCE(BLANK_CALCULATOR_CLASS_NAMESPACE, "a blank calculator class property.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * calculator class property that doesn't refer to an existing class.
     */
    public void test1CtorThrowsOnInvalidCalculatorClass() {
        check1CtorThrowsCE(INVALID_CALCULATOR_CLASS_NAMESPACE,
                "a calculator class property that doesn't refer to an existing class.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * calculator class property that doesn't derive from ScoreCalculator.
     */
    public void test1CtorThrowsOnUnderivedCalculatorClass() {
        check1CtorThrowsCE(UNDERIVED_CALCULATOR_CLASS_NAMESPACE,
                "a calculator class property that doesn't derive from ScoreCalculator.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * calculator class property that has a private zero argument constructor.
     */
    public void test1CtorThrowsOnNoExistCalculatorClassCtor() {
        check1CtorThrowsCE(NOEXIST_CALCULATOR_CLASS_CTOR_NAMESPACE,
                "a calculator class property that doesn't have a public zero argument constructor.");
    }

    /**
     * Ensures that the one argument constructor throws a ConfigurationException when given a namespace with a
     * calculator class property that has a private namespace constructor.
     */
    public void test1CtorThrowsOnNoExistCalculatorClassNSCtor() {
        check1CtorThrowsCE(NOEXIST_CALCULATOR_CLASS_NS_CTOR_NAMESPACE,
            "a calculator class property that doesn't have a public namespace constructor.");
    }

    /**
     * Helper method to check that the one argument constructor throws a ConfigurationException when given the
     * specified namespace.
     *
     * @param   namespace
     *          The namespace that should cause an ConfigurationException.
     * @param   message
     *          The message in the error message if the unit test fails.
     */
    private void check1CtorThrowsCE(String namespace, String message) {
        try {
            new CalculationManager(namespace);
            fail("A ConfigurationException is expected when given a namespace with " + message);
        } catch (ConfigurationException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the one argument constructor sets the calculators field properly, by checking that the
     * getScoreCalculator method returns an object of the correct type.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void test1CtorSetScoreCalculator() throws ConfigurationException {
        CalculationManager manager = new CalculationManager(VALID_NAMESPACE);

        assertTrue(
                "Question type 1 should be a BinaryScoreCalculator.",
                manager.getScoreCalculator(1) instanceof BinaryScoreCalculator);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) Tests

    /**
     * Ensures that the four argument boolean constructor throws an IllegalArgumentException when given a null
     * questionTypes list.
     */
    public void test4BooleanCtorThrowsOnNullQuestionTypes() {
        questionTypes = null;
        check4BooleanCtorThrowsIAE("a null questionTypes list.");
    }

    /**
     * Ensures that the four argument boolean constructor throws an IllegalArgumentException when given a
     * questionTypes list with a negative value.
     */
    public void test4BooleanCtorThrowsOnNegativeQuestionType() {
        questionTypes = new long[] {-1};
        check4BooleanCtorThrowsIAE("a questionTypes list with a negative value.");
    }

    /**
     * Ensures that the four argument boolean constructor throws an IllegalArgumentException when given a
     * questionTypes list with a zero value.
     */
    public void test4BooleanCtorThrowsOnZeroQuestionType() {
        questionTypes = new long[] {0};
        check4BooleanCtorThrowsIAE("a questionTypes list with a zero value.");
    }

    /**
     * Ensures that the four argument boolean constructor throws an IllegalArgumentException when given a
     * questionTypes list with a duplicate value.
     */
    public void test4BooleanCtorThrowsOnDuplicateQuestionType() {
        questionTypes = new long[] {1, 1};
        calculators = new ScoreCalculator[] {new BinaryScoreCalculator(), new BinaryScoreCalculator()};
        check4BooleanCtorThrowsIAE("a questionTypes list with a duplicate value.");
    }

    /**
     * Ensures that the four argument boolean constructor throws an IllegalArgumentException when given a
     * questionTypes and calculators lists that have different lengths.
     */
    public void test4BooleanCtorThrowsOnDifferentLengths() {
        questionTypes = new long[] {1, 2, 3, 4, 5};
        calculators = new ScoreCalculator[] {new BinaryScoreCalculator(), new BinaryScoreCalculator()};
        check4BooleanCtorThrowsIAE("a questionTypes and calculators lists that have different lengths.");
    }

    /**
     * Ensures that the four argument boolean constructor throws an IllegalArgumentException when given a null
     * calculators lists.
     */
    public void test4BooleanCtorThrowsOnNullCalculators() {
        calculators = null;
        check4BooleanCtorThrowsIAE("a null calculators list.");
    }

    /**
     * Ensures that the four argument boolean constructor throws an IllegalArgumentException when given a
     * calculators lists with a null calculator.
     */
    public void test4BooleanCtorThrowsOnNullCalculator() {
        calculators = new ScoreCalculator[] {null};
        check4BooleanCtorThrowsIAE("a calculators list with a null calculator.");
    }

    /**
     * Ensures that the four argument boolean constructor throws an IllegalArgumentException when given a null
     * builder.
     */
    public void test4BooleanCtorThrowsOnNullBuilder() {
        builder = null;
        check4BooleanCtorThrowsIAE("a null builder.");
    }

    /**
     * Helper method to check that the four argument boolean constructors throws an IllegalArgumentException with
     * the current member fields.
     *
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void check4BooleanCtorThrowsIAE(String message) {
        try {
            new CalculationManager(questionTypes, calculators, builder, useCaching);
            fail("An IllegalArgumentException is expected when given " + message);
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the four argument boolean constructor sets the calculators field properly, by checking that
     * the getScoreCalculator method returns an object of the correct type.
     */
    public void test4BooleanCtorSetScoreCalculator() {
        CalculationManager manager = new CalculationManager(questionTypes, calculators, builder, useCaching);

        assertTrue(
                "Question type 1 should be a BinaryScoreCalculator.",
                manager.getScoreCalculator(1) instanceof BinaryScoreCalculator);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(long[], ScoreCalculator[], ScorecardMatrixBuilder, Cache) Tests

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a null
     * questionTypes list.
     */
    public void test4CacheCtorThrowsOnNullQuestionTypes() {
        questionTypes = null;
        check4CacheCtorThrowsIAE("a null questionTypes list.");
    }

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a
     * questionTypes list with a negative value.
     */
    public void test4CacheCtorThrowsOnNegativeQuestionType() {
        questionTypes = new long[] {-1};
        check4CacheCtorThrowsIAE("a questionTypes list with a negative value.");
    }

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a
     * questionTypes list with a zero value.
     */
    public void test4CacheCtorThrowsOnZeroQuestionType() {
        questionTypes = new long[] {0};
        check4CacheCtorThrowsIAE("a questionTypes list with a zero value.");
    }

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a
     * questionTypes list with a duplicate value.
     */
    public void test4CacheCtorThrowsOnDuplicateQuestionType() {
        questionTypes = new long[] {1, 1};
        calculators = new ScoreCalculator[] {new BinaryScoreCalculator(), new BinaryScoreCalculator()};
        check4CacheCtorThrowsIAE("a questionTypes list with a duplicate value.");
    }

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a
     * questionTypes and calculators lists that have different lengths.
     */
    public void test4CacheCtorThrowsOnDifferentLengths() {
        questionTypes = new long[] {1, 2, 3, 4, 5};
        calculators = new ScoreCalculator[] {new BinaryScoreCalculator(), new BinaryScoreCalculator()};
        check4CacheCtorThrowsIAE("a questionTypes and calculators lists that have different lengths.");
    }

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a null
     * calculators lists.
     */
    public void test4CacheCtorThrowsOnNullCalculators() {
        calculators = null;
        check4CacheCtorThrowsIAE("a null calculators list.");
    }

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a
     * calculators lists with a null calculator.
     */
    public void test4CacheCtorThrowsOnNullCalculator() {
        calculators = new ScoreCalculator[] {null};
        check4CacheCtorThrowsIAE("a calculators list with a null calculator.");
    }

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a null
     * builder.
     */
    public void test4CacheCtorThrowsOnNullBuilder() {
        builder = null;
        check4CacheCtorThrowsIAE("a null builder.");
    }

    /**
     * Ensures that the four argument Cache constructor throws an IllegalArgumentException when given a null
     * cache.
     */
    public void test4CacheCtorThrowsOnNullCache() {
        cache = null;
        check4CacheCtorThrowsIAE("a null cache.");
    }

    /**
     * Helper method to check that the four argument Cache constructors throws an IllegalArgumentException with
     * the current member fields.
     *
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void check4CacheCtorThrowsIAE(String message) {
        try {
            new CalculationManager(questionTypes, calculators, builder, cache);
            fail("An IllegalArgumentException is expected when given " + message);
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the four argument Cache constructor sets the calculators field properly, by checking that
     * the getScoreCalculator method returns an object of the correct type.
     */
    public void test4CacheCtorSetScoreCalculator() {
        CalculationManager manager = new CalculationManager(questionTypes, calculators, builder, cache);

        assertTrue(
                "Question type 1 should be a BinaryScoreCalculator.",
                manager.getScoreCalculator(1) instanceof BinaryScoreCalculator);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // addScoreCalculator Tests

    /**
     * Ensures that the addScoreCalculator method throws an IllegalArgumentException when given a negative
     * question type.
     */
    public void testAddSCThrowsOnNegativeQuestionType() {
        checkAddSCThrowsIAE(-1, new BinaryScoreCalculator(), "a negative question type.");
    }

    /**
     * Ensures that the addScoreCalculator method throws an IllegalArgumentException when given a zero
     * question type.
     */
    public void testAddSCThrowsOnZeroQuestionType() {
        checkAddSCThrowsIAE(0, new BinaryScoreCalculator(), "a zero question type.");
    }

    /**
     * Ensures that the addScoreCalculator method throws an IllegalArgumentException when given a null score
     * calculator.
     */
    public void testAddSCThrowsOnNullScoreCalculator() {
        checkAddSCThrowsIAE(1, null, "a null score calculator.");
    }

    /**
     * Check that the addScoreCalculator method throws an IllegalArgumentException when given the specified
     * arguments.
     *
     * @param   questionType
     *          The question type that should cause an IllegalArgumentException.
     * @param   calculator
     *          The score calculator that should cause an IllegalArgumentException.
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void checkAddSCThrowsIAE(long questionType, ScoreCalculator calculator, String message) {
        try {
            instance.addScoreCalculator(questionType, calculator);
            fail("An IllegalArgumentException is expected when given a " + message);
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the addScoreCalculator method properly adds the score calculator, by checking that the
     * getScoreCalculator method returns the original instance.
     */
    public void testAddSCAddedScoreCalculator() {
        ScoreCalculator calculator = new BinaryScoreCalculator();
        instance.addScoreCalculator(1, calculator);

        assertSame("The score calculator was not added properly.", calculator, instance.getScoreCalculator(1));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // removeScoreCalculator Tests

    /**
     * Ensures that the removeScoreCalculator throws an IllegalArgumentException when given a negative question
     * type.
     */
    public void testRemoveSCThrowsOnNegativeQuestionType() {
        try {
            instance.removeScoreCalculator(-1);
            fail("An IllegalArgumentException is expected when given a negative question type.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the removeScoreCalculator throws an IllegalArgumentException when given a zero question
     * type.
     */
    public void testRemoveSCThrowsOnZeroQuestionType() {
        try {
            instance.removeScoreCalculator(0);
            fail("An IllegalArgumentException is expected when given a zero question type.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the removeScoreCalculator returns the removed score calculator (when it exists).
     */
    public void testRemoveSCReturnedScoreCalculator() {
        ScoreCalculator calculator = new BinaryScoreCalculator();
        instance.addScoreCalculator(1, calculator);

        assertSame("The removed calculator should be returned.", calculator, instance.removeScoreCalculator(1));
    }

    /**
     * Ensures that the removeScoreCalculator returns a null (when it doesn't exist).
     */
    public void testRemoveSCReturnedNull() {
        assertNull("A null calculator should be returned.", instance.removeScoreCalculator(1));
    }

    /**
     * Ensures that the removeScoreCalculator removed the score calculator.
     */
    public void testRemoveSCRemovedScoreCalculator() {
        instance.addScoreCalculator(1, new BinaryScoreCalculator());
        instance.removeScoreCalculator(1);

        assertNull("A null calculator should be returned.", instance.getScoreCalculator(1));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // getScoreCalculator Tests

    /**
     * Ensures that the getScoreCalculator throws an IllegalArgumentException when given a negative question type.
     */
    public void testGetSCThrowsOnNegativeQuestionType() {
        try {
            instance.getScoreCalculator(-1);
            fail("An IllegalArgumentException is expected when given a negative question type.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the getScoreCalculator throws an IllegalArgumentException when given a zero question type.
     */
    public void testGetSCThrowsOnZeroQuestionType() {
        try {
            instance.getScoreCalculator(0);
            fail("An IllegalArgumentException is expected when given a zero question type.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // clearScoreCalculators Tests

    /**
     * Ensures that the clearScoreCalculators removed the score calculator.
     */
    public void testClearSCRemovedScoreCalculator() {
        instance.addScoreCalculator(1, new BinaryScoreCalculator());
        instance.clearScoreCalculators();

        assertNull("A null calculator should be returned.", instance.getScoreCalculator(1));
    }
}
