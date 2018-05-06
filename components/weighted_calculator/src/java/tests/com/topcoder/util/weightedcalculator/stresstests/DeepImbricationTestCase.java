/**
 * DeepImbricationTestCase.java
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.weightedcalculator.stresstests;

import com.topcoder.util.weightedcalculator.*;
import java.util.*;
import junit.framework.*;

/**
 * Stress test checks the behavior on matrices with many imbricated sections,
 * each having one item and one child section.
 *
 * @author adic
 * @version 1.0
 */
public class DeepImbricationTestCase extends TestCase {

    private static final int LEVELS=1500; // imbrication level
    private static final int TIMEOUT=3000; // timeout in ms for the test
    private static final double EPS=1e-3; // error tolerance
    private static final long SEED=1234; // fixed random seed ensures deterministic behavior

    /**
     * Stress test checks the behavior on matrices with many imbricated sections,
     * each having one item and one child section.
     */
    public void testDeepImbrication() {
        long tic=System.currentTimeMillis();
        Random rand=new Random(SEED);
        MathMatrix matrix=new MathMatrix("test matrix",1000);
        assertNotNull("MathMatrix object should not be null",matrix);
        double scores[]=new double[LEVELS];
        double weights[]=new double[LEVELS];
        Object lastSection=matrix;
        double lastWeight=0;

        // create imbricated sections iteratively
        for (int i=0;i<LEVELS;i++) {
            // create one section and fill it with one item
            double weight=(i==LEVELS-1)?1:rand.nextDouble();
            Section section=new Section("section",1-lastWeight);
            assertNotNull("Section object should not be null",section);
            double score=rand.nextDouble()*1000;
            Item item=new LineItem("line",weight,1000,score);
            assertNotNull("LineItem object should not be null",item);
            section.addItem(item);

            // update last parent
            if (lastSection instanceof MathMatrix) ((MathMatrix)lastSection).addItem(section);
            else ((Section)lastSection).addItem(section);
            lastWeight=weight;
            lastSection=section;
            scores[i]=score;
            weights[i]=weight;

            assertTrue("Time limit exceeded",System.currentTimeMillis()-tic<TIMEOUT);
        }

        // check corectness
        for (int i=LEVELS-2;i>=0;i--) scores[i]=scores[i]*weights[i]+scores[i+1]*(1-weights[i]);
        assertTrue("Incorrect weighted score",Math.abs(scores[0]-matrix.getWeightedScore())<EPS);

        // timeout ?
        long tac=System.currentTimeMillis();
        assertTrue("Time limit exceeded",tac-tic<TIMEOUT);
        System.out.println("OK "+(tac-tic)+"ms");
    }

    public static Test suite() {
        return new TestSuite(DeepImbricationTestCase.class);
    }

    public static void main(String params[]) {
        new DeepImbricationTestCase().testDeepImbrication();
    }
}

