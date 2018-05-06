/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.failuretests;

import java.io.File;
import java.util.Iterator;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Failure test cases for class <code>BinaryScoreCalculator </code>.
 *
 * @author Chenhong
 * @version 1.0
 */
public class FailureTestForBinaryScoreCalculator extends TestCase {
    /**
     * Represents the config file for BinaryScoreCalculator class.
     */
    private static final String CONFIG_FILE = "test_files/failuretests/BinaryScoreCalculatorConfig.xml";

    /**
     * Represents the BinaryScoreCalculator instance for test.
     */
    private MyBinaryScoreCalculator calculator = null;

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

        calculator = new MyBinaryScoreCalculator();
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
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the namespace is null, IllegalArgumentException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_1() throws Exception  {
        try {
            new BinaryScoreCalculator(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the namespace is empty string, IllegalArgumentException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_2() throws Exception  {
        try {
            new BinaryScoreCalculator("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the namespace is empty string, IllegalArgumentException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_3() throws Exception  {
        try {
            new BinaryScoreCalculator("                       ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the positive answer property is missing, ConfigurationException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_4() throws Exception  {
        String namespace = "missing_positive_answer";
        try {
            new BinaryScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the positive answer property is empty string, ConfigurationException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_5() throws Exception  {
        String namespace = "empty_positive_answer";
        try {
            new BinaryScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }


    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the positive answer property is empty string, ConfigurationException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_6() throws Exception  {
        String namespace = "Empty_String_positive_answer";
        try {
            new BinaryScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the negative answer property is missing, ConfigurationException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_7() throws Exception  {
        String namespace = "missing_negative_answer";
        try {
            new BinaryScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the negative answer is empty, ConfigurationException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_8() throws Exception  {
        String namespace = "empty_negative_answer";
        try {
            new BinaryScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the negative answer is empty string, ConfigurationException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_9() throws Exception  {
        String namespace = "empty_stirng_negative_answer";
        try {
            new BinaryScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String namespace) </code>.
     * If the negative answer is same to positive answer, ConfigurationException should be thrown.
     * @throws Exception to junit.
     */
    public void testBinaryScoreCalculatorString_10() throws Exception  {
        String namespace = "invalid_same";
        try {
            new BinaryScoreCalculator(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String positiveAnswer, String negativeAnswer) </code>.
     * If any parameter is null, or empty string, or two argument is same, IllegalArgumentException should be thrown.
     */
    public void testBinaryScoreCalculatorStringString_1() {
        try {
            new BinaryScoreCalculator(null, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String positiveAnswer, String negativeAnswer) </code>.
     * If any parameter is null, or empty string, or two argument is same, IllegalArgumentException should be thrown.
     */
    public void testBinaryScoreCalculatorStringString_2() {
        try {
            new BinaryScoreCalculator("  ", "");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>BinaryScoreCalculator(String positiveAnswer, String negativeAnswer) </code>.
     * If any parameter is null, or empty string, or two argument is same, IllegalArgumentException should be thrown.
     */
    public void testBinaryScoreCalculatorStringString_3() {
        try {
            new BinaryScoreCalculator("same", "same");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateAnswer(String answer) </code>.
     * If the parameter answer is null, or empty, IllegalArgumentException should be thrown.
     *
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
     * Test method <code> float evaluateAnswer(String answer) </code>.
     * If the parameter answer is null, or empty, IllegalArgumentException should be thrown.
     *
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
     * Test method <code> float evaluateAnswer(String answer) </code>.
     * The  answer is neither equal to the positive nor negative answer, or ScoreCalculatorException should throw.
     *
     * @throws Exception to junit.
     */
    public void testEvaluateAnswer_3() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            calculator.evaluateAnswer("none_exit");
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateItem(Item item, Question question) </code>.
     * If item is null, IllegalArgumentException should be thrown.
     * @throws Exception to junit.
     */
    public void testEvaluateItem_1() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            calculator.evaluateItem(null, new Question(1));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateItem(Item item, Question question) </code>.
     * If question is null, IllegalArgumentException should be thrown.
     * @throws Exception to junit.
     */
    public void testEvaluateItem_2() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            Item item = new Item();
            item.setId(1);
            item.setAnswer("answer");


            calculator.evaluateItem(new Item(), null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateItem(Item item, Question question) </code>.
     * The item's question does not match the question's id, ScoreCalculatorException should be thrown.
     * @throws Exception to junit.
     */
    public void testEvaluateItem_3() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            Item item = new Item();
            item.setId(1);
            item.setAnswer("answer");
            item.setQuestion(1);


            calculator.evaluateItem(new Item(), new Question(1));
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateItem(Item item, Question question) </code>.
     * The question's id is not set, ScoreCalculatorException should be thrown.
     * @throws Exception to junit.
     */
    public void testEvaluateItem_4() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            Item item = new Item();
            item.setId(1);
            item.setAnswer("answer");
            item.setQuestion(1);


            calculator.evaluateItem(new Item(), new Question());
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateItem(Item item, Question question) </code>.
     * The the answer is not instance of String, ScoreCalculatorException should be thrown.
     * @throws Exception to junit.
     */
    public void testEvaluateItem_5() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            Item item = new Item();
            item.setId(1);
            item.setAnswer(new Integer(10));
            item.setQuestion(1);


            calculator.evaluateItem(new Item(), new Question(10));
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateItem(Item item, Question question) </code>.
     * The answer is empty string, ScoreCalculatorException should be thrown.
     * @throws Exception to junit.
     */
    public void testEvaluateItem_6() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            Item item = new Item();
            item.setId(1);
            item.setAnswer("              ");
            item.setQuestion(1);


            calculator.evaluateItem(new Item(), new Question(10));
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateItem(Item item, Question question) </code>.
     * The answer is not valid as value or value/scale, ScoreCalculatorException should be thrown.
     * @throws Exception to junit.
     */
    public void testEvaluateItem_7() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            Item item = new Item();
            item.setId(1);
            item.setAnswer("2/1");
            item.setQuestion(1);


            calculator.evaluateItem(new Item(), new Question(10));
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }

    /**
     * Test method <code> float evaluateItem(Item item, Question question) </code>.
     * The answer is not valid as value or value/scale, ScoreCalculatorException should be thrown.
     * @throws Exception to junit.
     */
    public void testEvaluateItem_8() throws Exception {
        calculator = new MyBinaryScoreCalculator("valid");
        try {
            Item item = new Item();
            item.setId(1);
            item.setAnswer("0");
            item.setQuestion(1);


            calculator.evaluateItem(new Item(), new Question(10));
            fail("ScoreCalculatorException is expected.");
        } catch (ScoreCalculatorException e) {
            // Ok.
        }
    }
}
