package com.topcoder.util.weightedcalculator.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

import com.topcoder.util.weightedcalculator.*;

/**
 * Accuracy tests
 *
 * @author  dpozdol
 * @version 1.0
 * @version Copyright C 2002, TopCoder, Inc. All rights reserved
 */
public class MathMatrixTests extends TestCase {

    public MathMatrixTests(String testName) {
        super(testName);
    }

   public void testMathMatrixStuff() {

        MathMatrix tester = new MathMatrix("tester", 57);
        assertTrue(tester.getDescription().equals("tester"));
        assertTrue(tester.getWeight()==(1));
        assertTrue(tester.getMaximumScore()==(57));

        tester.addItem(new LineItem("1", 0.312, 1));
        tester.addItem(new LineItem("2", 0.312, 1));
        tester.addItem(new Section("3", 0.312));

        assertTrue(tester.getItem(0).getDescription().equals("1"));
        assertTrue(tester.getItem(1).getDescription().equals("2"));
        assertTrue(tester.getItem(2).getDescription().equals("3"));

        assertTrue(tester.removeItem(2).getDescription().equals("3"));
        assertTrue(tester.removeItem(1).getDescription().equals("2"));
        assertTrue(tester.removeItem(0).getDescription().equals("1"));
        assertTrue(tester.getItems().isEmpty());

        Section temp = new Section("tester", 0.6);
        temp.addItem(new LineItem("1", 0.3, 1, 0.5));
        temp.addItem(new LineItem("2", 0.35, 2, 1.5));
        temp.addItem(new LineItem("3", 0.15, 3, 0));
        temp.addItem(new LineItem("4", 0.2, 4, 3));
        MathMatrix tester2 = new MathMatrix("test 2", 50);
        tester2.addItem(temp);

        Section temp2 = new Section("test", 0.6);
        temp2.addItem(new LineItem("1", 0.3, 1, 0.5));
        temp2.addItem(new LineItem("2", 0.35, 2, 1.5));
        temp2.addItem(new LineItem("3", 0.15, 3, 0));
        temp2.addItem(new LineItem("4", 0.2, 4, 3));
        MathMatrix test4 = new MathMatrix("test 2", 50);
        test4.addItem(temp2);
        test4.addItem(new LineItem("5", 0.4, 10, 3));
        assertTrue(test4.getWeightedScore()==(
        50*(0.6*(0.3*0.5/1+0.35*1.5/2+0.15*0/3+0.2*3/4)+0.4*3/10)));
        temp2.getItem(1).setWeight(0.35);

    }

    public static Test suite() {
        return new junit.framework.TestSuite(MathMatrixTests.class);
    }

}
