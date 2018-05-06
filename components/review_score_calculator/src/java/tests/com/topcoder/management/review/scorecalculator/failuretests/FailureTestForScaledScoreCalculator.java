/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.failuretests;

import java.io.File;
import java.util.Iterator;

import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.calculators.ScaledScoreCalculator;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Failure test cases for class <code>ScaledScoreCalculator </code>.
 *
 * @author Chenhong
 * @version 1.0
 */
public class FailureTestForScaledScoreCalculator extends TestCase {

    /**
     * Represents the config file for ScaledScoreCalculator class.
     */
    private static final String CONFIG_FILE = "test_files/failuretests/ScaledScoreCalculatorConfig.xml";

    /**
     * Represents the ScaledScoreCalculator instance for test.
     */
    private MyScaledScoreCalculator calculator = null;

    /**
     * Set up the enviroment.
     *
     * @throws Exception
     *             to junit.
     */
    public void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
        File file = new File(CONFIG_FILE);
        cm.add(file.getAbsolutePath());

        calculator = new MyScaledScoreCalculator();
    }

    /**
     * Tear down the enviroment. Clear all the namespaces in the config manager.
     *
     * @throws Exception
     *             to junit.
     */
    public void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }

    /**
     * Test the default constructor.
     */
    public void testScaledScoreCalculator() {
        assertNotNull("Should not be null.", calculator);
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(long defaultScale) </code>.
     */
    public void testScaledScoreCalculatorlong_1() {
        try {
            new ScaledScoreCalculator(0);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(long defaultScale) </code>.
     */
    public void testScaledScoreCalculatorlong_2() {
        try {
            new ScaledScoreCalculator(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(long defaultScale) </code>.
     */
    public void testScaledScoreCalculatorlong_3() {
        try {
            new ScaledScoreCalculator(-2);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_1() throws Exception {
        try {
            new ScaledScoreCalculator(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_2() throws Exception {
        try {
            new ScaledScoreCalculator("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_3() throws Exception {
        try {
            new ScaledScoreCalculator("     ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_4() throws Exception {
        String namespace = "invalid1";
        try {
            new ScaledScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_5() throws Exception {
        String namespace = "invalid2";
        try {
            new ScaledScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_6() throws Exception {
        String namespace = "invalid3";
        try {
            new ScaledScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_7() throws Exception {
        String namespace = "invalid4";
        try {
            new ScaledScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_8() throws Exception {
        String namespace = "invalid5";
        try {
            new ScaledScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_9() throws Exception {
        String namespace = "negative_1";
        try {
            new ScaledScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_10() throws Exception {
        String namespace = "negative_2";
        try {
            new ScaledScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }


    /**
     * Test constructor <code>ScaledScoreCalculator(String namespace)</code>.
     * @throws Exception to junit.
     */
    public void testScaledScoreCalculatorString_11() throws Exception {
        String namespace = "zero";
        try {
            new ScaledScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_1() throws Exception {
        try {
            calculator.evaluateAnswer(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_2() throws Exception {
        try {
            calculator.evaluateAnswer("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_3() throws Exception {
        try {
            calculator.evaluateAnswer("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * If the default scale is -1, the parameter must be in the format of value/scale.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_4() throws Exception {
        try {
            calculator.evaluateAnswer("4");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * If the default scale is -1, the parameter must be in the format of value/scale.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_5() throws Exception {
        try {
            calculator.evaluateAnswer("1.3");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * If the default scale is -1, the parameter must be in the format of value/scale.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_6() throws Exception {
        try {
            calculator.evaluateAnswer("-0.8");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * The value should not positive integer.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_7() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("-0.8");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * This case is valid, should not throw ScoreCalculatorException.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_8() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("2");
        } catch (ScoreCalculatorException e) {
            fail("ScoreCalculatorException is expected.");
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * The value should not be > scale.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_9() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("5/4");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_10() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("5/");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_11() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("////");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_12() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("/5/4");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * The value or scale should be positive integer.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_13() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("3.0/4");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_14() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("3/-4");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_15() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("tc/4");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_16() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("3/tc");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_17() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("3/1");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_18() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("1/0");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float evaluateAnswer(String answer) </code>.
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_19() throws Exception {
        calculator = new MyScaledScoreCalculator("valid");

        try {
            calculator.evaluateAnswer("1/1000000000000000000000000000000000000000000000000000");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }
}
