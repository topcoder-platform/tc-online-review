/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;
import java.io.File;
import java.util.Iterator;

import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;
/**
 * Tests for BinaryScoreCalculator class.
 * @author qiucx0161
 * @version 1.0
 */
public class TestBinaryScoreCalculator extends TestCase {

    /** BinaryScoreCalculatorExtend instance used for testing. */
    private BinaryScoreCalculatorExtend calc = null;

    /**
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        calc = new BinaryScoreCalculatorExtend("Yes", "No");
    }

    /**
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test evaluateAnswer method with accuracy case. the answer is yes.
     */
    public void testEvaluateAnswer1() throws Exception {
        assertEquals("The result of cacluating is wrong.", 1.0f, calc.evaluateAnswer("Yes"), 0);
    }

    /**
     * Test evaluateAnswer method with accuracy case. the answer is no.
     */
    public void testEvaluateAnswer2() throws Exception {
        assertEquals("The result of cacluating is wrong.", 0, calc.evaluateAnswer("No"), 0);
    }

    /**
     * Test evaluateAnswer method with accuracy case. the scalar used the default value,
     * which is loaded from configurable file.
     */
    public void testEvaluateAnswer3() throws Exception {
        clearAllNamespaces();
        ConfigManager.getInstance().add(new File("test_files/accuracy/Config.xml").getAbsolutePath());

        BinaryScoreCalculatorExtend ssce = new BinaryScoreCalculatorExtend(
                "BinaryScoreCalculator.accuracy.test");
        assertEquals("The result of cacluating is wrong.", 1.0f, ssce.evaluateAnswer("Pass"), 0);
        assertEquals("The result of cacluating is wrong.", 0, ssce.evaluateAnswer("Fail"), 0);
        clearAllNamespaces();
    }

    /**
     * Clear all the namespaces in this component
     */
    private void clearAllNamespaces() {
        try {
            for (Iterator iter = ConfigManager.getInstance().getAllNamespaces();
                    iter.hasNext();) {
                ConfigManager.getInstance().removeNamespace((String) iter.next());
            }
        } catch (Exception e) {
            // ignore.
        }
    }

    /**
     * BinaryScoreCalculatorExtend instance used for testing.
     */
    final class BinaryScoreCalculatorExtend extends BinaryScoreCalculator {
        /**
         * Creates a new BinaryScoreCalculatorExtend with the specified preconfigured
         * scale.
         *
         * @param defaultScale The preconfigured scale to initialize with.
         *
         * @throws IllegalArgumentException The defaultScale is not a positive long.
         */
        public BinaryScoreCalculatorExtend(String positiveAnswer, String negativeAnswer) {
            super(positiveAnswer, negativeAnswer);
        }

        /**
         * <p>
         * Creates a new BinaryScoreCalculator with the specified configuration
         * namespace.
         * </p>
         *
         * <p>
         * This constructor reads in the optional 'default_scale' property from the given
         * configuration namespace. If the property does not exist, then the undefined
         * value (-1) will be used.
         * </p>
         *
         * @param namespace The configuration namespace to read the preconfigured scale
         *        setting from.
         *
         * @throws ConfigurationException The namespace does not exist, the property
         *         exists but is not a positive or valid long.
         * @throws IllegalArgumentException The namespace is null or an empty string.
         */
        public BinaryScoreCalculatorExtend(String namespace)
            throws ConfigurationException {
            super(namespace);
        }

        /**
         * <p>
         * Evalutes the given answer and returns a value between 0 and 1, inclusive.
         * </p>
         *
         * <p>
         * The answer must be in the format "value" or "value/scale", where "value" and
         * "scale" must both be parsable as positive long data types. The latter format
         * must be used when there is no preconfigured scale. The "value" must be less
         * than or equal to the "scale" (or the preconfigured scale, if there is no
         * "scale").
         * </p>
         *
         * @param answer The answer to be evaluated into a score between 0 and 1,
         *        inclusive.
         *
         * @return The score for the specified answer, which is just "value" divided by
         *         "scale" (where scale is either provided in the answer or by the
         *         default set in the constructor).
         *
         * @throws IllegalArgumentException The answer is a null reference or an empty
         *         string.
         * @throws ScoreCalculatorException The answer is not in a recognized format (see
         *         above) or is otherwise invalid.
         */
        protected float evaluateAnswer(String answer) throws ScoreCalculatorException {
            return super.evaluateAnswer(answer);
        }
    }
}
